package com.fivos.temporalpoc.batch;

import java.util.Set;

public class BatchChildWorkflowImpl implements BatchChildWorkflow {
	@Override
	public Boolean executeWorkflow(Set<Integer> values) {
		for (Integer value : values) {
			System.out.println("Processing: " + value);
		}
		return true;
	}
}
