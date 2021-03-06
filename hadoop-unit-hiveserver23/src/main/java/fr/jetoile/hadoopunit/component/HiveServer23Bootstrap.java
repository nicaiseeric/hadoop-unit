/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fr.jetoile.hadoopunit.component;

import com.github.sakserv.minicluster.impl.HiveLocalServer2;
import com.github.sakserv.minicluster.util.WindowsLibsUtils;
import fr.jetoile.hadoopunit.ComponentMetadata;
import fr.jetoile.hadoopunit.HadoopUtils;
import fr.jetoile.hadoopunit.exception.BootstrapException;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hive.conf.HiveConf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.Map;

public class HiveServer23Bootstrap implements BootstrapHadoop3 {
    static final private Logger LOGGER = LoggerFactory.getLogger(HiveServer23Bootstrap.class);

    private HiveLocalServer2 hiveLocalServer2;

    private State state = State.STOPPED;

    private Configuration configuration;
    private String host;
    private int port;
    private String scratchDirectory;
    private String warehouseDirectory;
    private String zookeeperConnectionString;
    private String hostMetastore;
    private int portMetastore;

    private String hdfsUri;

    public HiveServer23Bootstrap() {
        if (hiveLocalServer2 == null) {
            try {
                configuration = HadoopUtils.INSTANCE.loadConfigFile(null);
                loadConfig();
            } catch (BootstrapException e) {
                LOGGER.error("unable to load configuration", e);
            }
        }
    }

    public HiveServer23Bootstrap(URL url) {
        if (hiveLocalServer2 == null) {
            try {
                configuration = HadoopUtils.INSTANCE.loadConfigFile(url);
                loadConfig();
            } catch (BootstrapException e) {
                LOGGER.error("unable to load configuration", e);
            }
        }
    }

    @Override
    public ComponentMetadata getMetadata() {
        return new HiveServer23Metadata();
    }

    @Override
    public String getProperties() {
        return "\n \t\t\t port:" + port;
    }

    private void loadConfig() throws BootstrapException {
        host = configuration.getString(Hive3Config.HIVE3_SERVER2_HOSTNAME_KEY);
        port = configuration.getInt(Hive3Config.HIVE3_SERVER2_PORT_KEY);
        hostMetastore = configuration.getString(Hive3Config.HIVE3_METASTORE_HOSTNAME_CLIENT_KEY);
        portMetastore = configuration.getInt(Hive3Config.HIVE3_METASTORE_PORT_KEY);
        scratchDirectory = getTmpDirPath(configuration, Hive3Config.HIVE3_SCRATCH_DIR_KEY);
        warehouseDirectory = configuration.getString(Hive3Config.HIVE3_WAREHOUSE_DIR_KEY);
        zookeeperConnectionString = configuration.getString(ZookeeperConfig.ZOOKEEPER_HOST_CLIENT_KEY) + ":" + configuration.getInt(ZookeeperConfig.ZOOKEEPER_PORT_KEY);
        hdfsUri = "hdfs://" + configuration.getString(Hdfs3Config.HDFS3_NAMENODE_HOST_CLIENT_KEY) + ":" + configuration.getString(Hdfs3Config.HDFS3_NAMENODE_PORT_KEY);
    }

    @Override
    public void loadConfig(Map<String, String> configs) {
        if (StringUtils.isNotEmpty(configs.get(Hive3Config.HIVE3_SERVER2_HOSTNAME_KEY))) {
            host = configs.get(Hive3Config.HIVE3_SERVER2_HOSTNAME_KEY);
        }
        if (StringUtils.isNotEmpty(configs.get(Hive3Config.HIVE3_SERVER2_PORT_KEY))) {
            port = Integer.parseInt(configs.get(Hive3Config.HIVE3_SERVER2_PORT_KEY));
        }
        if (StringUtils.isNotEmpty(configs.get(Hive3Config.HIVE3_METASTORE_HOSTNAME_CLIENT_KEY))) {
            hostMetastore = configs.get(Hive3Config.HIVE3_METASTORE_HOSTNAME_CLIENT_KEY);
        }
        if (StringUtils.isNotEmpty(configs.get(Hive3Config.HIVE3_METASTORE_PORT_KEY))) {
            portMetastore = Integer.parseInt(configs.get(Hive3Config.HIVE3_METASTORE_PORT_KEY));
        }
        if (StringUtils.isNotEmpty(configs.get(Hive3Config.HIVE3_SCRATCH_DIR_KEY))) {
            scratchDirectory = getTmpDirPath(configs, Hive3Config.HIVE3_SCRATCH_DIR_KEY);
        }
        if (StringUtils.isNotEmpty(configs.get(Hive3Config.HIVE3_WAREHOUSE_DIR_KEY))) {
            warehouseDirectory = configs.get(Hive3Config.HIVE3_WAREHOUSE_DIR_KEY);
        }
        if (StringUtils.isNotEmpty(configs.get(ZookeeperConfig.ZOOKEEPER_HOST_CLIENT_KEY)) && StringUtils.isNotEmpty(configs.get(ZookeeperConfig.ZOOKEEPER_PORT_KEY))) {
            zookeeperConnectionString = configs.get(ZookeeperConfig.ZOOKEEPER_HOST_CLIENT_KEY) + ":" + configs.get(ZookeeperConfig.ZOOKEEPER_PORT_KEY);
        }
        if (StringUtils.isNotEmpty(configs.get(Hdfs3Config.HDFS3_NAMENODE_HOST_CLIENT_KEY)) && StringUtils.isNotEmpty(configs.get(Hdfs3Config.HDFS3_NAMENODE_PORT_KEY))) {
            hdfsUri = "hdfs://" + configs.get(Hdfs3Config.HDFS3_NAMENODE_HOST_CLIENT_KEY) + ":" + Integer.parseInt(configs.get(Hdfs3Config.HDFS3_NAMENODE_PORT_KEY));
        }
    }


    private void cleanup() {
    }

    private void build() {
        try {
            Class.forName("org.apache.calcite.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            LOGGER.error("Unable to load Calcite JDBC driver", e);
        }

        hiveLocalServer2 = new HiveLocalServer2.Builder()
                .setHiveServer2Hostname(host)
                .setHiveServer2Port(port)
                .setHiveMetastoreHostname(hostMetastore)
                .setHiveMetastorePort(portMetastore)
                .setHiveWarehouseDir(warehouseDirectory)
                .setHiveScratchDir(scratchDirectory)
                .setHiveConf(buildHiveConf())
                .setZookeeperConnectionString(zookeeperConnectionString)
                .build();

    }

    private HiveConf buildHiveConf() {
        // Handle Windows
        WindowsLibsUtils.setHadoopHome();

        HiveConf hiveConf = new HiveConf();
        hiveConf.set("fs.defaultFS", hdfsUri);

        hiveConf.set("metastore.metastore.event.db.notification.api.auth", "false");

        return hiveConf;
    }

    @Override
    public Bootstrap start() {
        if (state == State.STOPPED) {
            state = State.STARTING;
            LOGGER.info("{} is starting", this.getClass().getName());

            build();
            try {
                hiveLocalServer2.start();
            } catch (Exception e) {
                LOGGER.error("unable to add hiveserver23", e);
            }
            state = State.STARTED;
            LOGGER.info("{} is started", this.getClass().getName());
        }
        return this;
    }

    @Override
    public Bootstrap stop() {
        if (state == State.STARTED) {
            state = State.STOPPING;
            LOGGER.info("{} is stopping", this.getClass().getName());
            try {
                hiveLocalServer2.stop(true);
            } catch (Exception e) {
                LOGGER.error("unable to stop hiveserver23", e);
            }
            cleanup();
            state = State.STOPPED;
            LOGGER.info("{} is stopped", this.getClass().getName());
        }
        return this;
    }

    @Override
    public org.apache.hadoop.conf.Configuration getConfiguration() {
        return hiveLocalServer2.getHiveConf();
    }

}
