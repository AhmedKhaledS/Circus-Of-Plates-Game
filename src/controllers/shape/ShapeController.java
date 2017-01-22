package controllers.shape;

import controllers.shape.util.ShapeFallingObserver;
import controllers.shape.util.ShapeMovingObserver;
import javafx.scene.Node;
import models.Platform;
import models.shapes.Shape;
import models.states.ShapeState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ShapeController<T extends Node> implements ShapeFallingObserver,
		ShapeMovingObserver {
	static Logger logger = LogManager.getLogger(ShapeController.class);
	private final T shape;
	private final Shape shapeModel;
	private final Platform platform;
	private ShapeMovementController<T> currentController;
	public ShapeController(final T shape, final Shape model,
						   final models.Platform platform) {
		this.shape = shape;
		this.shapeModel = model;
		this.platform = platform;
		currentController = null;
		System.out.println(platform.getWidth());
	}

	public void startMoving() {
		logger.debug("Shape " + " Movement Requested");
		switch (shapeModel.getState()) {
			case MOVING_HORIZONTALLY:
				currentController
						= new MovingShapeController<>(
								shape, shapeModel,
						platform, this);
				break;
			case FALLING:
				currentController
						= new FallingShapeController<>(shape,
						shapeModel, this);
				break;
			default:
				break;
		}
	}

	@Override
	public void shapeShouldStartFalling() {
	    logger.debug("A Shape Should Start Falling");
		if (currentController == null) {
			return;
		}
		currentController.stopMoving();
		shapeModel.setState(ShapeState.FALLING);
		currentController
				= new FallingShapeController<>(shape, shapeModel, this);
	}

	@Override
	public void shapeShouldStopFalling() {
		if (currentController == null) {
			return;
		}
		shapeModel.setState(ShapeState.ON_THE_GROUND);
		currentController.stopMoving();
		//TODO:- Ask the main controller to add the plate to the pool.
	}

	public void shapeFellOnTheStack() {
		if (currentController == null) {
			return;
		}
		shapeModel.setState(ShapeState.ON_THE_STACK);
		currentController.stopMoving();
	}
}