package de.haw.utils;

import de.haw.tree.TemplateInstance;
import de.haw.tree.TreeNode;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Dots {
    public static void treeToDot(String name, TreeNode<TemplateInstance> tree) {
        var file = createOrClearFile(name + ".dot");

        try (FileOutputStream fos = new FileOutputStream(file)) {
            var sb = new StringBuilder();
            sb.append("digraph {").append(System.lineSeparator());
            sb.append("omega [label=\"ω\"]").append(System.lineSeparator());

            int i = 0;
            for (var node : tree) {
                var word = node.isEmpty() ? "ε" : node.getData().getTemplate().getWord();
                sb.append(i).append(" [label=\"").append(word).append("\"]").append(System.lineSeparator());
                i++;
            }

            sb.append("omega -> 0").append(System.lineSeparator());
            int j = 0, k = 1;
            for (var node : tree) {
                for (var x = 1; x <= node.getChildren().size(); x++) {
                    sb.append(j).append(" -> ").append(k).append(System.lineSeparator());
                    k++;
                }
                j++;
            }

            sb.append("}");
            fos.write(sb.toString().getBytes());
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static File createOrClearFile(String fileName) {
        var dir = new File(fileName);
        var exists = dir.exists();
        if (exists) {
            try (FileOutputStream fos = new FileOutputStream(dir)) {
                fos.write("".getBytes());
                fos.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return dir;
    }
}
