package de.haw.pipeline.pipe;

import de.haw.lsystem.LSystem;
import de.haw.module.Estimator;
import de.haw.tree.TemplateInstance;
import de.haw.tree.TreeNode;

import java.util.Random;

public class PipelineContext {
    public LSystem lSystem;
    public TreeNode<TemplateInstance> tree;
    // Weighting parameter to control generated L-System production rule set size
    public float wL;
    // Weight balancing between grammar length and grammar edit distance
    public float w0;
    public Random randomizer;
    public Estimator estimator;
}
