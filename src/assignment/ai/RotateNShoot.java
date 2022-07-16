package assignment.ai;

import assignment.input.Action;
import assignment.input.Controller;

public class RotateNShoot implements Controller {

	private Action action;
	
	public RotateNShoot() {
		action = new Action();
	}
	
	@Override
	public Action getAction() {
		action.shoot = true;
		action.turn = 1;
		return action;
	}
}
