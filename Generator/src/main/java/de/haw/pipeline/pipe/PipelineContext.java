package de.haw.pipeline.pipe;

import de.haw.lsystem.LSystem;
import de.haw.tree.TemplateInstance;
import de.haw.tree.TreeNode;

public class PipelineContext {
    public LSystem lSystem;
    public TreeNode<TemplateInstance> tree;
    // Weighting parameter to control generated L-System production rule set size
    public float wL;
}
