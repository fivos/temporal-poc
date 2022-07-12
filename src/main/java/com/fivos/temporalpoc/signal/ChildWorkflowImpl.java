package com.fivos.temporalpoc.signal;

public class ChildWorkflowImpl implements ChildWorkflow {
	@Override
	public void executeWorkflow() {
		System.out.println("Running child workflow");
	}
}
