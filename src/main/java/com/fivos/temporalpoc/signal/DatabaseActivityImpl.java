package com.fivos.temporalpoc.signal;

import java.util.Random;
import java.util.stream.Stream;

import com.fivos.temporalpoc.activity.ParentWorkflow;
import io.temporal.activity.Activity;
import io.temporal.client.WorkflowClient;
import io.temporal.serviceclient.WorkflowServiceStubs;

public class DatabaseActivityImpl implements DatabaseActivity {
	@Override
	public Boolean loadData() {
		WorkflowServiceStubs service = WorkflowServiceStubs.newLocalServiceStubs();
		WorkflowClient client = WorkflowClient.newInstance(service);
		SignalParentWorkflow workflow = client.newWorkflowStub(
			SignalParentWorkflow.class, Activity.getExecutionContext().getInfo().getWorkflowId());

		try (Stream<Integer> stream = query()) {
			stream.forEach(workflow::sendSignal);
		}

		return true;
	}

	private Stream<Integer> query() {
		// TODO: Replace with DB query
		Random random = new Random();
		return random.ints(2).boxed();
	}
}
