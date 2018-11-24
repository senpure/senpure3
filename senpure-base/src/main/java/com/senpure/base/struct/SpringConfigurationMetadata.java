package com.senpure.base.struct;

import java.util.ArrayList;
import java.util.List;

/**
 * SpringConfigurationMetadata
 *
 * @author senpure
 * @time 2018-11-19 09:47:44
 */
public class SpringConfigurationMetadata {

    private List<Group> groups;
    private List<Property> properties;
    private List<Hint> hints=new ArrayList<>();

    public void addGroup(Group group) {
        if (groups == null) {
            groups = new ArrayList<>(16);
        }
        groups.add(group);
    }
    public void addProperty(Property property) {
        if (properties== null) {
            properties= new ArrayList<>(16);
        }

        properties.add(property);
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public List<Property> getProperties() {
        return properties;
    }

    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }

    public List<Hint> getHints() {
        return hints;
    }

    public void setHints(List<Hint> hints) {
        this.hints = hints;
    }

    public static class Hint {
        private String name;

        private List<ValueHint> values;
        private List<ValueProvider> providers;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<ValueHint> getValues() {
            return values;
        }

        public void setValues(List<ValueHint> values) {
            this.values = values;
        }

        public List<ValueProvider> getProviders() {
            return providers;
        }

        public void setProviders(List<ValueProvider> providers) {
            this.providers = providers;
        }

        public static class ValueHint {

            private Object value;
            private String description;

            public Object getValue() {
                return value;
            }

            public void setValue(Object value) {
                this.value = value;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }
        }

        public static class ValueProvider {

            private String name;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
    }

    public static class Group {
        private String name;
        private String type;
        private String description;
        private String sourceType;
        private String sourceMethod;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getSourceType() {
            return sourceType;
        }

        public void setSourceType(String sourceType) {
            this.sourceType = sourceType;
        }

        public String getSourceMethod() {
            return sourceMethod;
        }

        public void setSourceMethod(String sourceMethod) {
            this.sourceMethod = sourceMethod;
        }
    }

    public static class Property {
        private String name;
        private String type;
        private String description;
        private String sourceType;
        private Object defaultValue;
        private Boolean deprecated;
        private Deprecation deprecation;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getSourceType() {
            return sourceType;
        }

        public void setSourceType(String sourceType) {
            this.sourceType = sourceType;
        }

        public Object getDefaultValue() {
            return defaultValue;
        }

        public void setDefaultValue(Object defaultValue) {
            this.defaultValue = defaultValue;
        }

        public Boolean getDeprecated() {
            return deprecated;
        }

        public void setDeprecated(Boolean deprecated) {
            this.deprecated = deprecated;
        }

        public Deprecation getDeprecation() {
            return deprecation;
        }

        public void setDeprecation(Deprecation deprecation) {
            this.deprecation = deprecation;
        }
    }

    public static class Deprecation {
        private String level = "error";
        private String replacement;

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public String getReplacement() {
            return replacement;
        }

        public void setReplacement(String replacement) {
            this.replacement = replacement;
        }
    }
}
