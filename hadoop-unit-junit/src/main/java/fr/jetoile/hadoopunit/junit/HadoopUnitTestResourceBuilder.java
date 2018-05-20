/*
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package fr.jetoile.hadoopunit.junit;

import fr.jetoile.hadoopunit.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class HadoopUnitTestResourceBuilder {
    private Map<String, String> properties = new HashMap<>();
    private List<ComponentArtifact> components = new ArrayList<>();
    private String path;
    private String repositoryManager = "https://repo.maven.apache.org/maven2/";

    public static HadoopUnitTestResourceBuilder forJunit() {
        return new HadoopUnitTestResourceBuilder();
    }

    public HadoopUnitTestResourceBuilder with(String name, String groupId, String artifactId, String version) {
        ComponentArtifact componentArtifact = new ComponentArtifact(name, groupId, artifactId, version);
        this.components.add(componentArtifact);
        return this;
    }

    public HadoopUnitTestResourceBuilder whereMavenLocalRepo(String path) {
        this.path = path;
        return this;
    }

    public HadoopUnitTestResourceBuilder whereRepositoryManager(String repositoryManager) {
        this.repositoryManager = repositoryManager;
        return this;
    }

    public HadoopUnitTestResourceBuilder withProperties(String key, String value) {
        properties.put(key, value);
        return this;
    }

    public HadoopUnitTestResource build() {
        return new HadoopUnitTestResource(repositoryManager, path, components);
    }
}
