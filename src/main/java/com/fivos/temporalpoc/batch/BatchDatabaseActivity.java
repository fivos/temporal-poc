package com.fivos.temporalpoc.batch;


import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface BatchDatabaseActivity {

	@ActivityMethod
	Boolean loadData();

}
