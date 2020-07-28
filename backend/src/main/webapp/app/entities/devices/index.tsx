import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Devices from './devices';
import DevicesDetail from './devices-detail';
import DevicesUpdate from './devices-update';
import DevicesDeleteDialog from './devices-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={DevicesDeleteDialog} />
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={DevicesUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={DevicesUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={DevicesDetail} />
      <ErrorBoundaryRoute path={match.url} component={Devices} />
    </Switch>
  </>
);

export default Routes;
