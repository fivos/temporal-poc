package com.fivos.temporalpoc.signal;

public class ChildWorkflowImpl implements ChildWorkflow {
	@Override
	public void runWorkflow() {
		System.out.println("Running child workflow");
	}
}
