package io.terrafino.api.ac.ado;

import io.terrafino.api.ac.AcException;
import io.terrafino.api.ac.attribute.Attributes;
import io.terrafino.api.ac.service.AcConnection;
import io.terrafino.api.ac.service.AcQueryService;
import io.terrafino.api.ac.service.AcService;
import io.terrafino.api.ac.service.AcUpdateService;
import io.terrafino.api.ac.timeseries.TsHeader;
import io.terrafino.api.ac.timeseries.TsRecords;
import io.terrafino.api.ac.validation.TsValidation;
import io.terrafino.api.ac.value.Value;
import io.terrafino.api.ac.value.ValueFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class Ado {

    private AcConnection conn;

    private String id;
    private String longname;
    private String template;

    private ConcurrentHashMap<String, Value> staticData;

    public Ado(String id, String longname, String template, AcConnection conn) {
        this.id = id;
        this.longname = longname;
        this.template = template;
        this.conn = conn;
        this.staticData = new ConcurrentHashMap<>();
    }

    public Ado withTemplate(String newTemplate) throws AcException {
        return new AcService(conn).createAdo(id, longname, newTemplate);
    }

    public String getId() {
        return id;
    }

    public String getLongname() {
        return longname;
    }

    public void setLongname(String longname) {
        this.longname = longname;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public void set(String attributeId, Value value) {
        this.staticData.put(attributeId, value);
    }

    public void set(String attributeId, String value) {
        set(attributeId, ValueFactory.createValue(value));
    }

    public void setAndStore(String attributeId, String value) throws AcException {
        set(attributeId, ValueFactory.createValue(value));
        store();
    }

    public Ado withAttrValue(String attributeId, Value value) throws AcException {
        set(attributeId, value);
        return this;
    }

    public Value get(String attributeId) {
        return this.staticData.getOrDefault(attributeId, ValueFactory.createNA());
    }

    public Attributes getAttributes() {
        ArrayList<String> attributes = Collections.list(staticData.keys());
        Collections.sort(attributes);
        return new Attributes(attributes);
    }

    public Ado createInAc() throws AcException {
        conn.getUpdateService().create(this);
        return this;
    }

    public boolean existsInAc() throws AcException {
        return conn.getQueryService().exists(this);
    }

    public void delete() throws AcException {
        conn.getUpdateService().delete(this);
    }

    public void deleteTimeseries(String tree) throws AcException {
        conn.getUpdateService().deleteTimeseries(this, tree);
    }

    public void store() throws AcException {
        conn.getUpdateService().store(this);
    }

    public Value load(String attr) throws AcException {
        Value value = conn.getQueryService().load(this, Collections.singletonList(attr)).get(0);
        this.staticData.put(attr, value);
        return value;
    }

    public Optional<TsHeader> getTimeseriesHeader(String tree) throws AcException {
        return conn.getQueryService().getTimeseriesHeader(this, tree);
    }

    public Optional<List<TsValidation>> getTimeseriesValidations(String tree) throws AcException {
        return conn.getQueryService().getTsValidations(this, tree);
    }

    public void removeAllTimeseriesValidations(String tree) throws AcException {
        conn.getUpdateService().removeAllTimeseriesValidations(this, tree);
    }

    public void removeTimeseriesValidation(String tree, TsValidation validation) throws AcException {
        conn.getUpdateService().removeTimeseriesValidation(this, tree, validation);
    }

    public void addTimeseriesValidation(String tree, TsValidation validation) throws AcException {
        conn.getUpdateService().addTimeseriesValidation(this, tree, validation);
    }

    public void amendTimeseriesHeaderWith(String tree, Attributes attributes) throws AcException {
        conn.getUpdateService().amendTimeseriesHeaderWith(this, tree, attributes);
    }

    public void storeTimeseries(String tree, TsRecords records) throws AcException {
        conn.getUpdateService().storeTimeseries(this, tree, records);
    }

    public TsRecords loadTimeseries(String tree, int start, int end, Attributes attributes) throws AcException {
        return conn.getQueryService().loadTimeseries(this, tree, start, end, attributes);
    }

    public void revalidate(String tree, int startDate, int endDate) throws AcException {
        conn.getUpdateService().revalidate(this, tree, startDate, 0, endDate, 235959);
    }

    public void revalidate(String tree) throws AcException {
        conn.getUpdateService().revalidate(this, tree, 19700101, 0, 29991231, 235959);
    }
}
