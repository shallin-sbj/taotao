package com.taotao.jedis;

import org.junit.Test;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

import java.util.HashSet;
import java.util.Set;

public class TestJedis {

    private String URL = "120.79.130.20";
//    private String URL = "192.168.16.145";

    /**
     *  6379 单机版
     * @throws Exception
     */

    @Test
    public void testjedis() throws Exception {
        //创建一个jedis对象，需要指定服务的ip和端口号
        Jedis jedis = new Jedis(URL, 6379);
        //直接操作数据库
//        jedis.set("12", "12");
        String result = jedis.get("jedis-key");
        System.out.println(result);
        //关闭jedis
        jedis.close();
    }

    @Test
    public void testJedisPool() throws Exception {
        //创建一个数据库连接池对象（单例），需要指定服务的ip和端口号
        JedisPool jedisPool = new JedisPool(URL, 6379);
        //从连接池中获得连接
        Jedis jedis = jedisPool.getResource();
        //使用Jedis操作数据库（方法级别使用）
        String result = jedis.get("jedis-key");
        System.out.println(result);
        //一定要关闭Jedis连接
        jedis.close();
        //系统关闭前关闭连接池
        jedisPool.close();
    }

    @Test
    public void testJedisCluster() throws Exception {
        Set<HostAndPort> nodes = new HashSet<>();
        nodes.add(new HostAndPort(URL, 7001));
        nodes.add(new HostAndPort(URL, 7002));
        nodes.add(new HostAndPort(URL, 7003));
        nodes.add(new HostAndPort(URL, 7004));
        nodes.add(new HostAndPort(URL, 7005));
        nodes.add(new HostAndPort(URL, 7006));
        JedisCluster cluster = new JedisCluster(nodes );
//        cluster.set("key1", "1000");
        System.out.println(cluster.get("key1"));
    }

    @Test
    public void testJedisCluster2() throws Exception {
        //创建一个JedisCluster对象，构造参数Set类型，集合中每个元素是HostAndPort类型
        Set<HostAndPort> nodes = new HashSet<>();
        //向集合中添加节点
        nodes.add(new HostAndPort(URL, 7001));
        nodes.add(new HostAndPort(URL, 7002));
        nodes.add(new HostAndPort(URL, 7003));
        nodes.add(new HostAndPort(URL, 7004));
        nodes.add(new HostAndPort(URL, 7005));
        nodes.add(new HostAndPort(URL, 7006));
        JedisCluster jedisCluster = new JedisCluster(nodes);
        //直接使用JedisCluster操作redis，自带连接池。jedisCluster对象可以是单例 的。
        jedisCluster.set("cluster-test", "hello jedis cluster");
        String string = jedisCluster.get("cluster-test");
        System.out.println(string);
        //系统关闭前关闭JedisCluster
        jedisCluster.close();
    }

}
