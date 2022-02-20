package org.ideadebugplugin;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class JsonNode {
    @JsonProperty("variable")
    private String nodeValue;

    @JsonProperty("innerVariables")
    private List<JsonNode> children = null;

    public String getNodeValue() {
        return nodeValue;
    }

    public void setNodeValue(String nodeValue) {
        this.nodeValue = nodeValue;
    }

    public List<JsonNode> getChildren() {
        return children;
    }

    public void setChildren(List<JsonNode> children) {
        this.children = children;
    }
}
