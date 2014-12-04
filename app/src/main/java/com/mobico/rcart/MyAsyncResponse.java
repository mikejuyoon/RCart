package com.mobico.rcart;

// =========== MyAsyncResponse Interface ==============
// ------------- Michael Yoon --------------
// Interface for executing actions in Activities after AsyncTasks.
// See MyHttpPost or MyHttpGet.
// Also implement in Activity.

public interface MyAsyncResponse {
    void processFinish(String output);
}