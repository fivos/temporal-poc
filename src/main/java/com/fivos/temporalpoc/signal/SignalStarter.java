package com.fivos.temporalpoc.signal;

import com.fivos.temporalpoc.activity.ParentWorkflowImpl;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;

public class SignalStarter {

	public static final String SIGNAL_TASK_QUEUE = "SIGNAL_TASK_QUEUE";
	private static final WorkflowServiceStubs service = WorkflowServiceStubs.newLocalServiceStubs();
	private static final WorkflowClient client = WorkflowClient.newInstance(service);
	private static final WorkerFactory factory = WorkerFactory.newInstance(client);

	public static void main(String[] args) {
		createWorker();

		WorkflowOptions parentWorkflowOptions =
			WorkflowOptions.newBuilder()
				.setWorkflowId("parentWorkflow")
				.setTaskQueue(SIGNAL_TASK_QUEUE)
				.build();
		SignalParentWorkflow parentWorkflowStub =
			client.newWorkflowStub(SignalParentWorkflow.class, parentWorkflowOptions);

		parentWorkflowStub.executeWorkflow();

		sleep(4);
	}


	private static void createWorker() {
		Worker worker = factory.newWorker(SIGNAL_TASK_QUEUE);
		worker.registerWorkflowImplementationTypes(SignalParentWorkflowImpl.class, ChildWorkflowImpl.class);
		worker.registerActivitiesImplementations(new DatabaseActivityImpl());

		factory.start();
	}

	private static void sleep(int seconds) {
		try {
			Thread.sleep(seconds * 1000);
		} catch (Exception e) {
			System.out.println("Exception: " + e.getMessage());
			System.exit(0);
		}
	}

}
