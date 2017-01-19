package controllers.shape;

import controllers.shape.util.ShapeFallingObserver;
import controllers.shape.util.ShapeMovingObserver;
import javafx.scene.Node;
import models.shapes.Shape;

public class ShapeController<T extends Node> implements ShapeFallingObserver,
ShapeMovingObserver {
	private final T shape;
	private final Shape shapeModel;
	ShapeMovementController<T> currentController;
	public ShapeController() {
		this.shape = shape;
		this.shapeModel = model;
		currentController = new MovingShapeController<>(shape, model, platform, this);
	}

	@Override
	public void shapeShouldStartFalling() {
		currentController.stopMoving();
		currentController
		= new FallingShapeController<>(shape, shapeModel, this);
	}

	@Override
	public void shapeShouldStopFalling() {
		currentController.stopMoving();
		//TODO:- Add the plate to the pool.
	}
}
