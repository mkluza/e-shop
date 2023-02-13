// @ts-ignore
import React from 'react';
import {Redirect, Route, Switch, useRouteMatch} from 'react-router';
import {HomePage} from '../pages/HomePage';
import {CartPage} from '../pages/CartPage';
import {ProductsPage} from '../pages/ProductsPage';
import {ProductPage} from '../pages/ProductPage';
import {UserPage} from '../pages/UserPage';
import {CategoryPage} from '../pages/CategoryPage';
import {OrderPage} from '../pages/OrderPage';
import {ThanksPage} from '../pages/ThanksPage';

export const MainView = () => {

	return (
		<>
			<Routes/>
		</>
	);
};

const Routes = () => {
	const {path} = useRouteMatch();

	return (
		<Switch>
			<Route path={path} exact component={HomePage}/>
			<Route path={path + 'cart'} component={CartPage}/>
			<Route path={path + 'products'} component={ProductsPage}/>
			<Route path={path + 'product/'} component={ProductPage}/>
			<Route path={path + 'user'} component={UserPage}/>
			<Route path={path + 'categories'} component={CategoryPage}/>
			<Route path={path + 'category/'} component={ProductsPage}/>
			<Route path={path + 'order'} component={OrderPage}/>
			<Route path={path + 'thanks'} component={ThanksPage}/>


			<Route exact path='*'>
				<Redirect to={{pathname: ''}}/>
			</Route>
		</Switch>
	);
};
