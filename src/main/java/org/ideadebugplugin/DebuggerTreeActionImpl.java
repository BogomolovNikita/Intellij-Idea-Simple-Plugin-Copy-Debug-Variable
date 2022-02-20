package org.ideadebugplugin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.intellij.debugger.memory.action.DebuggerTreeAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.xdebugger.impl.ui.tree.nodes.XValueNodeImpl;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class DebuggerTreeActionImpl extends DebuggerTreeAction {

    @Override
    protected void perform(XValueNodeImpl node, @NotNull String nodeName, AnActionEvent e) {
        JsonNode jsonRootNode = map(node);
        ObjectMapper objectMapper = new ObjectMapper().
                enable(SerializationFeature.INDENT_OUTPUT);
        try {
            String json = objectMapper.writeValueAsString(jsonRootNode);
            Toolkit.getDefaultToolkit()
                    .getSystemClipboard()
                    .setContents(
                            new StringSelection(json),
                            null
                    );
        } catch (IOException ex) {
            /*NOP*/
        }
    }


    private JsonNode map(XValueNodeImpl node) {
        JsonNode jsonNode = new JsonNode();
        String jsonNodeValue = node.getText().toString();
        jsonNode.setNodeValue(jsonNodeValue);
        List<XValueNodeImpl> children = getChildren(node);
        if (children != null && !children.isEmpty()) {
            final List<JsonNode> jsonNodeChildren = new ArrayList<>();
            jsonNode.setChildren(jsonNodeChildren);
            children.forEach(xValueNode -> jsonNodeChildren.add(map(xValueNode)));
        }
        return jsonNode;
    }

    private List<XValueNodeImpl> getChildren(XValueNodeImpl xValueNodeImpl) {
        List<XValueNodeImpl> children = new ArrayList<>();
        try {
            Class<? extends XValueNodeImpl> xValueNodeImplClass = xValueNodeImpl.getClass();
            Class<?> xValueNodeImplSuperclass = xValueNodeImplClass.getSuperclass();
            Field field = xValueNodeImplSuperclass.getDeclaredField("myValueChildren");
            field.setAccessible(true);
            Object fieldValue = field.get(xValueNodeImpl);
            if (fieldValue instanceof List<?>) {
                children = (List<XValueNodeImpl>) fieldValue;
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            /*NOP*/
        }
        return children;
    }
}
