import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Measures from './measures';
import MeasuresDetail from './measures-detail';
import MeasuresUpdate from './measures-update';
import MeasuresDeleteDialog from './measures-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={MeasuresDeleteDialog} />
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={MeasuresUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={MeasuresUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={MeasuresDetail} />
      <ErrorBoundaryRoute path={match.url} component={Measures} />
    </Switch>
  </>
);

export default Routes;
