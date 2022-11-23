// IRemoteService.aidl
package com.bitcnew.subpush;
import com.bitcnew.subpush.IReceivedCallback;

interface IRemoteService {
    int send(String reqUrl);
    void registerReceivedCallback(IReceivedCallback cb);
    void unregisterReceivedCallback(IReceivedCallback cb);
}

