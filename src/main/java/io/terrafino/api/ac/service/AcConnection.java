package io.terrafino.api.ac.service;

import com.ac.api.data.model.UserId;
import com.ac.api.data.statics.StaticFactory;
import com.ac.api.data.timeseries.TimeSeriesFactory;
import com.ac.api.error.ServerException;
import com.ac.api.factory.ApiFactory;
import com.ac.api.query.QueryFactory;
import com.ac.api.query.Result;
import com.ac.api.server.*;
import com.ac.api.transaction.TransactionFactory;
import io.terrafino.api.ac.AcException;
import io.terrafino.api.ac.AcProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class AcConnection {

    private static Logger log = LoggerFactory.getLogger(AcConnection.class);

    private static final int QUERY_SERVER_THREADS = 10;
    private static final int UPDATE_SERVER_THREADS = 10;

    private static final HttpConnectionInfo HTTP_INFO = null;
    private static final ConnectionInfo CONNECTION_INFO = ConnectionInfo.create(1800, CompressionLevel.NONE, HTTP_INFO);

    ApiFactory apiFactory;
    RemoteServer remoteServer;
    ServerInterface serverInterface;
    TransactionFactory transactionFactory;
    QueryFactory queryFactory;
    StaticFactory staticFactory;
    TimeSeriesFactory timeseriesFactory;

    private AcQueryService queryService;
    private AcUpdateService updateService;
    private AcMetaService metaService;

    private AcConnection() {
    }

    private AcConnection(AcProperties properties) throws AcException {
        try {
            this.apiFactory = ApiFactory.getInstance();
            this.transactionFactory = this.apiFactory.getTransactionFactory();
            this.queryFactory = this.apiFactory.getQueryFactory();
            this.staticFactory = this.apiFactory.getStaticFactory();
            this.timeseriesFactory = this.apiFactory.getTimeSeriesFactory();
            this.remoteServer = apiFactory.getServerFactory().createRemoteServer(QUERY_SERVER_THREADS, UPDATE_SERVER_THREADS);
            Installations installations = remoteServer.getInstallations(properties.getHost(), AcConnection.HTTP_INFO);
            Installation installation = installations.getInstallation(properties.getInstallation());
            log.info(String.format("Connecting as %s@%s.",  properties.getUser(), properties.getHost()));
            remoteServer.connect(installation, UserId.create(properties.getUser()), properties.getPassword(), CONNECTION_INFO);
            this.serverInterface = remoteServer.getServerInterface();

            this.queryService = new AcQueryService(this);
            this.updateService = new AcUpdateService(this);
            this.metaService = new AcMetaService(this);
        } catch (Exception e) {
            throw new AcException("Could not establish connection to AC!", e);
        }
    }

    public static AcConnection getDefaultConnection() throws AcException {
        log.debug("Creating default AC Connection.");
        AcProperties properties = AcProperties.fromResource("ac.properties");
        return new AcConnection(properties);
    }

    public AcQueryService getQueryService() {
        return this.queryService;
    }

    public AcUpdateService getUpdateService() {
        return this.updateService;
    }

    public AcMetaService getMetaService() {
        return this.metaService;
    }

    void executeTransaction(Transaction transaction, String msg) throws AcException {
        try {
            Result result = transaction.executeUsingConnection(this);
            if (result.hasError()) {
                throw new AcException(msg + ": " + result.getErrorMessage());
            }
        } catch (ServerException e) {
            throw new AcException(msg, e);
        }
    }

    public void disconnect() {
        remoteServer.disconnect();
    }

}
