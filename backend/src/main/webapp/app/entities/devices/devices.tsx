import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate, ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './devices.reducer';
import { IDevices } from 'app/shared/model/devices.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IDevicesProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const Devices = (props: IDevicesProps) => {
  useEffect(() => {
    props.getEntities();
  }, []);

  const { devicesList, match, loading } = props;
  return (
    <div>
      <h2 id="devices-heading">
        <Translate contentKey="backendApp.devices.home.title">Devices</Translate>
        <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
          <FontAwesomeIcon icon="plus" />
          &nbsp;
          <Translate contentKey="backendApp.devices.home.createLabel">Create new Devices</Translate>
        </Link>
      </h2>
      <div className="table-responsive">
        {devicesList && devicesList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="global.field.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="backendApp.devices.title">Title</Translate>
                </th>
                <th>
                  <Translate contentKey="backendApp.devices.type">Type</Translate>
                </th>
                <th>
                  <Translate contentKey="backendApp.devices.identification">Identification</Translate>
                </th>
                <th>
                  <Translate contentKey="backendApp.devices.active">Active</Translate>
                </th>
                <th>
                  <Translate contentKey="backendApp.devices.user">User</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {devicesList.map((devices, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${devices.id}`} color="link" size="sm">
                      {devices.id}
                    </Button>
                  </td>
                  <td>{devices.title}</td>
                  <td>
                    <Translate contentKey={`backendApp.Type.${devices.type}`} />
                  </td>
                  <td>{devices.identification}</td>
                  <td>{devices.active ? 'true' : 'false'}</td>
                  <td>{devices.user ? devices.user.email : ''}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${devices.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${devices.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${devices.id}/delete`} color="danger" size="sm">
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="backendApp.devices.home.notFound">No Devices found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

const mapStateToProps = ({ devices }: IRootState) => ({
  devicesList: devices.entities,
  loading: devices.loading
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(Devices);
