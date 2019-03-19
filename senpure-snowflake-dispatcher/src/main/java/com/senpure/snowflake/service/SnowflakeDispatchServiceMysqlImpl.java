package com.senpure.snowflake.service;

import com.senpure.base.util.Assert;
import com.senpure.snowflake.criteria.ServerCenterAndWorkCriteria;

import com.senpure.snowflake.mapper.ServerCenterAndWorkMapper;
import com.senpure.snowflake.mapper.SnowflakeLockMapper;

import com.senpure.snowflake.model.ServerCenterAndWork;
import com.senpure.snowflake.model.SnowflakeLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * ServerSnowflakeServiceMysqlImpl
 *
 * @author senpure
 * @time 2019-03-12 10:21:04
 */
@Service
public class SnowflakeDispatchServiceMysqlImpl implements SnowflakeDispatchService, ApplicationRunner {

    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private SnowflakeLockMapper lockMapper;

    @Autowired
    private ServerCenterAndWorkMapper serverCenterAndWorkMapper;

    private ThreadLocal<Integer> retry = ThreadLocal.withInitial(() -> 0);

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ServerCenterAndWork dispatch(String serverName, String serverKey) {
        SnowflakeLock lock = lockMapper.find(1L);
        if (lock == null) {
            logger.info("调度服务还没有准备好");
            return null;
        }
        ServerCenterAndWorkCriteria criteria = new ServerCenterAndWorkCriteria();
        criteria.setServerName(serverName);
        ServerCenterAndWork find = null;
        boolean saveFind = false;
        List<ServerCenterAndWork> serverCenterAndWorks = serverCenterAndWorkMapper.findByCriteria(criteria);
        if (serverCenterAndWorks.size() == 0) {
            find = new ServerCenterAndWork();
            find.setServerName(serverName);
            find.setServerKey(serverKey);
            find.setCenterId(0);
            find.setWorkId(0);
            saveFind = true;
        } else {
            //查找之前是否分配过
            for (ServerCenterAndWork work : serverCenterAndWorks) {
                if (work.getServerKey().equals(serverKey)) {
                    find = work;
                    break;
                }
            }
            if (find == null) {
                ServerCenterAndWork last = serverCenterAndWorks.get(serverCenterAndWorks.size() - 1);
                int centerId = last.getCenterId();
                int workId = last.getWorkId() + 1;
                if (workId >= 32) {
                    if (centerId++ >= 32) {
                        logger.info(" {} : {}  {} {} 数据中心与work中心已经不足", serverName, serverKey, centerId, workId);
                        return null;
                    }
                    workId = 0;
                }
                find = new ServerCenterAndWork();
                find.setServerName(serverName);
                find.setServerKey(serverKey);
                find.setCenterId(centerId);
                find.setWorkId(workId);
                saveFind = true;
            }
        }
        if (saveFind) {
            serverCenterAndWorkMapper.save(find);
        }
        ServerCenterAndWork result = new ServerCenterAndWork();
        result.setServerKey(serverKey);
        result.setServerName(serverName);
        result.setCenterId(find.getCenterId());
        result.setWorkId(find.getWorkId());
        //lock.setVersion(0);
        int i = lockMapper.update(lock);
        if (i == 1) {
            retry.set(0);
            return result;
        } else {
            int times = retry.get();
            if (times < 3) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    logger.error("", e);
                }
                retry.set(times + 1);
                logger.info(" {} : {} 第 {} 次重试", serverName, serverKey, retry.get());
                return dispatch(serverName, serverKey);
            }
            logger.info(" {} : {} 重试{}次失败", serverName, serverKey, retry.get());
            //抛出异常，进行事务回滚
            Assert.error("lock 更新失败 version:" + lock.getVersion());
            return null;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void run(ApplicationArguments args) throws Exception {
        SnowflakeLock lock = lockMapper.find(1L);
        if (lock == null) {
            lock = new SnowflakeLock();
            lock.setId(1L);
            lockMapper.save(lock);
        }
    }
}
