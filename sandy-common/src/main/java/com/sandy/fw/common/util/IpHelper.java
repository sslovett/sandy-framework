package com.sandy.fw.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.net.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * IP 工具类（整合版）
 *
 * 功能：
 * 1. getIpAddress() —— 获取当前 HTTP 请求的客户端真实 IP（适用于反向代理环境）
 * 2. getLocalIpAddress() —— 获取本机（服务器）在多网卡环境下的首选 IP（优先内网 IPv4）
 * 3. getIpAddressByInterface(String) —— 获取指定网卡的 IP
 * 4. getAllLocalIpAddresses() —— 获取所有有效网卡的 IPv4 地址
 *
 * @author lanhai (enhanced by Qwen)
 */
@Slf4j
public class IpHelper {

    private static final String UNKNOWN = "unknown";
    private static final List<String> LOCAL_IP_STRINGS = Arrays.asList("127.0.0.1", "0:0:0:0:0:0:0:1", "localhost");

    // ========================
    // 一、获取客户端请求 IP（Web 场景）
    // ========================

    /**
     * 获取当前 HTTP 请求的客户端真实 IP 地址
     * 适用于 Nginx、CDN 等反向代理部署场景
     *
     * @return 客户端 IP，若无法获取则返回 "127.0.0.1"
     */
    public static String getIpAddress() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return "127.0.0.1";
        }
        HttpServletRequest request = attributes.getRequest();

        String ip = extractValidIpFromHeader(request.getHeader("x-forwarded-for"));
        if (ip != null) return ip;

        ip = extractValidIpFromHeader(request.getHeader("Proxy-Client-IP"));
        if (ip != null) return ip;

        ip = extractValidIpFromHeader(request.getHeader("WL-Proxy-Client-IP"));
        if (ip != null) return ip;

        ip = request.getRemoteAddr();
        if (isValidIp(ip)) {
            return normalizeIpv6(ip);
        }

        return "127.0.0.1";
    }

    private static String extractValidIpFromHeader(String header) {
        if (header == null || header.isEmpty() || UNKNOWN.equalsIgnoreCase(header)) {
            return null;
        }

        List<String> ips = Arrays.stream(header.split(","))
                .map(String::trim)
                .filter(IpHelper::isValidIp)
                .collect(Collectors.toList());

        if (ips.isEmpty()) return null;

        // 从右往左找第一个非内网 IP（更安全）
        for (int i = ips.size() - 1; i >= 0; i--) {
            String ip = ips.get(i);
            if (!isLocalOrPrivateIp(ip)) {
                return normalizeIpv6(ip);
            }
        }

        // 全是内网？返回第一个（可能是内网用户）
        return normalizeIpv6(ips.get(0));
    }

    // ========================
    // 二、获取本机服务器 IP（多网卡场景）
    // ========================

    /**
     * 获取本机首选 IP 地址（优先内网 IPv4）
     *
     * 过滤：回环、虚拟、未启用网卡
     * 顺序：内网 IPv4 > 公网 IPv4 > fallback
     */
    public static String getLocalIpAddress() {
        try {
            List<String> siteLocalIps = new ArrayList<>();
            List<String> publicIps = new ArrayList<>();

            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface ni = interfaces.nextElement();
                if (ni.isLoopback() || !ni.isUp() || ni.isVirtual()) {
                    continue;
                }

                Enumeration<InetAddress> addresses = ni.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress addr = addresses.nextElement();
                    if (!(addr instanceof Inet4Address)) continue;

                    String ip = addr.getHostAddress();
                    if (addr.isSiteLocalAddress()) {
                        siteLocalIps.add(ip);
                    } else if (!addr.isAnyLocalAddress() &&
                            !addr.isLinkLocalAddress() &&
                            !addr.isLoopbackAddress()) {
                        publicIps.add(ip);
                    }
                }
            }

            if (!siteLocalIps.isEmpty()) {
                Collections.sort(siteLocalIps);
                return siteLocalIps.get(0);
            }
            if (!publicIps.isEmpty()) {
                Collections.sort(publicIps);
                return publicIps.get(0);
            }

            InetAddress localHost = InetAddress.getLocalHost();
            if (localHost instanceof Inet4Address) {
                return localHost.getHostAddress();
            }
        } catch (Exception e) {
             log.warn("Get local IP failed", e);
        }
        return "127.0.0.1";
    }

    /**
     * 获取指定网卡名称的 IPv4 地址
     */
    public static String getIpAddressByInterface(String interfaceName) {
        try {
            NetworkInterface ni = NetworkInterface.getByName(interfaceName);
            if (ni == null || !ni.isUp() || ni.isLoopback() || ni.isVirtual()) {
                return null;
            }
            Enumeration<InetAddress> addresses = ni.getInetAddresses();
            while (addresses.hasMoreElements()) {
                InetAddress addr = addresses.nextElement();
                if (addr instanceof Inet4Address && !addr.isLoopbackAddress()) {
                    return addr.getHostAddress();
                }
            }
        } catch (Exception e) {
             log.warn("Failed to get IP for interface: " + interfaceName, e);
        }
        return null;
    }

    /**
     * 获取所有有效网卡的 IPv4 地址映射
     */
    public static Map<String, List<String>> getAllLocalIpAddresses() {
        Map<String, List<String>> result = new LinkedHashMap<>();
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface ni = interfaces.nextElement();
                if (ni.isLoopback() || !ni.isUp() || ni.isVirtual()) continue;

                List<String> ips = Collections.list(ni.getInetAddresses()).stream()
                        .filter(addr -> addr instanceof Inet4Address)
                        .filter(addr -> !addr.isLoopbackAddress())
                        .map(InetAddress::getHostAddress)
                        .collect(Collectors.toList());

                if (!ips.isEmpty()) {
                    result.put(ni.getName(), ips);
                }
            }
        } catch (Exception e) {
             log.warn("Failed to get all local IPs", e);
        }
        return result;
    }

    // ========================
    // 通用辅助方法
    // ========================

    private static boolean isValidIp(String ip) {
        if (ip == null || ip.isEmpty()) return false;
        try {
            InetAddress addr = InetAddress.getByName(ip);
            return addr instanceof Inet4Address || addr instanceof Inet6Address;
        } catch (UnknownHostException e) {
            return false;
        }
    }

    private static boolean isLocalOrPrivateIp(String ip) {
        if (LOCAL_IP_STRINGS.contains(ip.toLowerCase())) {
            return true;
        }
        try {
            InetAddress addr = InetAddress.getByName(ip);
            return addr.isAnyLocalAddress() ||
                    addr.isLoopbackAddress() ||
                    addr.isLinkLocalAddress() ||
                    addr.isSiteLocalAddress();
        } catch (UnknownHostException e) {
            return false;
        }
    }

    private static String normalizeIpv6(String ip) {
        if ("0:0:0:0:0:0:0:1".equals(ip)) {
            return "::1";
        }
        return ip;
    }
}