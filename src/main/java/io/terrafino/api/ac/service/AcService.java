package io.terrafino.api.ac.service;

import io.terrafino.api.ac.AcException;
import io.terrafino.api.ac.ado.Ado;

import java.util.List;

public class AcService {

    private AcConnection conn;

    private static int counter = 1;

    public AcService(AcConnection conn) throws AcException {
        this.conn = conn;
    }

    public Ado createAdo(String id, String longname, String template) {
        return new Ado(id, longname, template, conn);
    }

    public Ado createAdo(String id) {
        return createAdo(id, "", "");
    }

    public Ado fromAcAdo(com.ac.api.data.statics.Ado ado) {
        return new Ado(ado.getId().getValue(), ado.getName(), "", conn);
    }

    public List<List<String>> executeFormulaEngineQuery(String fe) throws AcException {
        return conn.getQueryService().executeFormulaEngineQuery(fe);
    }

    public Ado testAdoWithPrefix(String prefix) {
        String counterStr = String.format("%04d", counter++);
        String id = String.format("%s.%s.TEST", prefix, counterStr);
        String longname = String.format("TEST ADO %s", counterStr);
        String template = "";
        return createAdo(id, longname, template);
    }


}
