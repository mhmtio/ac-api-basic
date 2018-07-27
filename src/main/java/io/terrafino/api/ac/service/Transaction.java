package io.terrafino.api.ac.service;

import com.ac.api.error.ServerException;
import com.ac.api.query.Result;

public interface Transaction {

    public Result executeUsingConnection(AcConnection conn) throws ServerException;
}
