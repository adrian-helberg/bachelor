package de.haw.pipeline.pipe;

import de.haw.gui.template.TemplateInstance;
import de.haw.gui.turtle.TurtleGraphic;
import de.haw.tool.Inferer;
import de.haw.pipeline.Pipe;
import de.haw.utils.Logging;
import de.haw.utils.Templates;
import java.util.HashMap;
import java.util.logging.Logger;
import static de.haw.gui.GeneratorController.showPopup;

/**
 * Pipe for executing the infer algorithm
 */
public class InfererPipe implements Pipe<PipelineContext, PipelineContext>, Logging {
    private static Logger logger;
    @Override
    public PipelineContext process(PipelineContext input) {
        if (logger == null) logger = getLogger();
        logger.info("Infer L-System from tree");
        logger.info(input.tree.toString());
        // Update pipeline context
        input.lSystem = new Inferer(input.tree).infer();
        logger.info(input.lSystem.toString());

        // Show evaluation popup
        var turtleGraphic = new TurtleGraphic(300,300);
        var p = new HashMap<String, Number>();
        p.put("Scaling", 1.0f);
        p.put("Rotation", 0.0f);
        p.put("Branching angle", 45.0f);
        TemplateInstance templateInstance = new TemplateInstance(Templates.populate(input.lSystem.derive(), p));
        turtleGraphic.parseWord(templateInstance, false);
        showPopup("Inferiertes L-System", turtleGraphic, input.lSystem.toString(), 10, 10);

        // Pass pipeline context to next pipe
        return input;
    }
}