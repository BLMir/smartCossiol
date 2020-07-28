import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { getEntity, updateEntity, createEntity, reset } from './devices.reducer';
import { IDevices } from 'app/shared/model/devices.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IDevicesUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const DevicesUpdate = (props: IDevicesUpdateProps) => {
  const [userId, setUserId] = useState('0');
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { devicesEntity, users, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/devices');
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getUsers();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...devicesEntity,
        ...values
      };
      entity.user = users[values.user];

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
          <h2 id="backendApp.devices.home.createOrEditLabel">
            <Translate contentKey="backendApp.devices.home.createOrEditLabel">Create or edit a Devices</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : devicesEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="devices-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="devices-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="titleLabel" for="devices-title">
                  <Translate contentKey="backendApp.devices.title">Title</Translate>
                </Label>
                <AvField
                  id="devices-title"
                  type="text"
                  name="title"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') }
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="typeLabel" for="devices-type">
                  <Translate contentKey="backendApp.devices.type">Type</Translate>
                </Label>
                <AvInput
                  id="devices-type"
                  type="select"
                  className="form-control"
                  name="type"
                  value={(!isNew && devicesEntity.type) || 'SmartCossiol'}
                >
                  <option value="SmartCossiol">{translate('backendApp.Type.SmartCossiol')}</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label id="identificationLabel" for="devices-identification">
                  <Translate contentKey="backendApp.devices.identification">Identification</Translate>
                </Label>
                <AvField id="devices-identification" type="text" name="identification" />
              </AvGroup>
              <AvGroup check>
                <Label id="activeLabel">
                  <AvInput id="devices-active" type="checkbox" className="form-check-input" name="active" />
                  <Translate contentKey="backendApp.devices.active">Active</Translate>
                </Label>
              </AvGroup>
              <AvGroup>
                <Label for="devices-user">
                  <Translate contentKey="backendApp.devices.user">User</Translate>
                </Label>
                <AvInput id="devices-user" type="select" className="form-control" name="user">
                  <option value="" key="0" />
                  {users
                    ? users.map((otherEntity, index) => (
                        <option value={index} key={otherEntity.id}>
                          {otherEntity.email}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/devices" replace color="info">
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
  users: storeState.userManagement.users,
  devicesEntity: storeState.devices.entity,
  loading: storeState.devices.loading,
  updating: storeState.devices.updating,
  updateSuccess: storeState.devices.updateSuccess
});

const mapDispatchToProps = {
  getUsers,
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(DevicesUpdate);
