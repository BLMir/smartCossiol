import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './measures.reducer';
import { IMeasures } from 'app/shared/model/measures.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IMeasuresDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const MeasuresDetail = (props: IMeasuresDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { measuresEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          <Translate contentKey="backendApp.measures.detail.title">Measures</Translate> [<b>{measuresEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="temp">
              <Translate contentKey="backendApp.measures.temp">Temp</Translate>
            </span>
          </dt>
          <dd>{measuresEntity.temp}</dd>
          <dt>
            <span id="soil">
              <Translate contentKey="backendApp.measures.soil">Soil</Translate>
            </span>
          </dt>
          <dd>{measuresEntity.soil}</dd>
          <dt>
            <span id="light">
              <Translate contentKey="backendApp.measures.light">Light</Translate>
            </span>
          </dt>
          <dd>{measuresEntity.light}</dd>
          <dt>
            <span id="insertAt">
              <Translate contentKey="backendApp.measures.insertAt">Insert At</Translate>
            </span>
          </dt>
          <dd>
            <TextFormat value={measuresEntity.insertAt} type="date" format={APP_DATE_FORMAT} />
          </dd>
          <dt>
            <Translate contentKey="backendApp.measures.devices">Devices</Translate>
          </dt>
          <dd>{measuresEntity.devices ? measuresEntity.devices.identification : ''}</dd>
        </dl>
        <Button tag={Link} to="/measures" replace color="info">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/measures/${measuresEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ measures }: IRootState) => ({
  measuresEntity: measures.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(MeasuresDetail);
