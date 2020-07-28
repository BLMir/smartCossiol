import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IDevices } from 'app/shared/model/devices.model';
import { getEntities as getDevices } from 'app/entities/devices/devices.reducer';
import { getEntity, updateEntity, createEntity, reset } from './measures.reducer';
import { IMeasures } from 'app/shared/model/measures.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IMeasuresUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const MeasuresUpdate = (props: IMeasuresUpdateProps) => {
  const [devicesId, setDevicesId] = useState('0');
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { measuresEntity, devices, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/measures');
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getDevices();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    values.insertAt = convertDateTimeToServer(values.insertAt);

    if (errors.length === 0) {
      const entity = {
        ...measuresEntity,
        ...values
      };

      if (isNew) {
        props.createEntity(entity);
      } else {
        props.updateEntity(entity);
      }
    }
  };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="backendApp.measures.home.createOrEditLabel">
            <Translate contentKey="backendApp.measures.home.createOrEditLabel">Create or edit a Measures</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : measuresEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="measures-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="measures-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="tempLabel" for="measures-temp">
                  <Translate contentKey="backendApp.measures.temp">Temp</Translate>
                </Label>
                <AvField
                  id="measures-temp"
                  type="string"
                  className="form-control"
                  name="temp"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    number: { value: true, errorMessage: translate('entity.validation.number') }
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="soilLabel" for="measures-soil">
                  <Translate contentKey="backendApp.measures.soil">Soil</Translate>
                </Label>
                <AvField
                  id="measures-soil"
                  type="string"
                  className="form-control"
                  name="soil"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    number: { value: true, errorMessage: translate('entity.validation.number') }
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="lightLabel" for="measures-light">
                  <Translate contentKey="backendApp.measures.light">Light</Translate>
                </Label>
                <AvField
                  id="measures-light"
                  type="string"
                  className="form-control"
                  name="light"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    number: { value: true, errorMessage: translate('entity.validation.number') }
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="insertAtLabel" for="measures-insertAt">
                  <Translate contentKey="backendApp.measures.insertAt">Insert At</Translate>
                </Label>
                <AvInput
                  id="measures-insertAt"
                  type="datetime-local"
                  className="form-control"
                  name="insertAt"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.measuresEntity.insertAt)}
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') }
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label for="measures-devices">
                  <Translate contentKey="backendApp.measures.devices">Devices</Translate>
                </Label>
                <AvInput id="measures-devices" type="select" className="form-control" name="devices.id">
                  <option value="" key="0" />
                  {devices
                    ? devices.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.identification}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/measures" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </AvForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

const mapStateToProps = (storeState: IRootState) => ({
  devices: storeState.devices.entities,
  measuresEntity: storeState.measures.entity,
  loading: storeState.measures.loading,
  updating: storeState.measures.updating,
  updateSuccess: storeState.measures.updateSuccess
});

const mapDispatchToProps = {
  getDevices,
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(MeasuresUpdate);
