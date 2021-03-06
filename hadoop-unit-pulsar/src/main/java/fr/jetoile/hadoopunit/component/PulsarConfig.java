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

public class PulsarConfig {

    // Pulsar
    public static final String PULSAR_NAME_KEY = "pulsar.name";
    public static final String PULSAR_IP_KEY = "pulsar.ip";
    public static final String PULSAR_PORT_KEY = "pulsar.port";
    public static final String PULSAR_HTTP_PORT_KEY = "pulsar.http.port";
    public static final String PULSAR_TEMP_DIR_KEY = "pulsar.temp.dir";
    public static final String PULSAR_STREAMER_STORAGE_PORT_KEY = "pulsar.streamer.storage.port";

    public static final String PULSAR_WORKER_ENABLED_KEY = "pulsar.worker.enabled";
    public static final String PULSAR_WORKER_CLIENT_AUTHENTICATION_PARAMETERS_KEY = "pulsar.worker.clientAuthenticationParameters";
    public static final String PULSAR_WORKER_CLIENT_AUTHENTICATION_PLUGIN_KEY = "pulsar.worker.clientAuthenticationPlugin";

    public static final String PULSAR_AUTHENTICATION_ENABLED_KEY = "pulsar.authentication.enabled";
    public static final String PULSAR_AUTHENTICATION_PROVIDERS_KEY = "pulsar.authentication.provider";
    public static final String PULSAR_AUTHORIZATION_ENABLED_KEY = "pulsar.authorization.enabled";
    public static final String PULSAR_AUTHORIZATION_PROVIDER_KEY = "pulsar.authorization.provider";
    public static final String PULSAR_EXTRA_CONF_KEY = "pulsar.extra.conf";

    public static final String PULSAR_IP_CLIENT_KEY = "pulsar.client.ip";

    private PulsarConfig() {}
}
