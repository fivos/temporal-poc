package com.fivos.temporalpoc.signal;

public class SignalChildWorkflowImpl implements SignalChildWorkflow {
	@Override
	public void executeWorkflow(Integer value) {
		System.out.println("Running child workflow with value: " + value);
	}
}
