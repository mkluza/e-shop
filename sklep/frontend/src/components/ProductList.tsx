// @ts-ignore
import React, {FC, useEffect, useState} from 'react';
import {Product, ProductProps} from './Product';
import {GridList, GridListTile} from '@material-ui/core';
import {RootStore} from '../stores/RootStore';
import {inject, observer} from 'mobx-react';
import {toJS} from 'mobx';
import {v4 as uuidv4} from 'uuid';
import styled from "styled-components";
import Cookies from "js-cookie";
import {useHistory} from "react-router";


export const ProductList: FC<{store?: RootStore}> = inject("store")(observer(({store}) => {
	const productsStore = store?.productsStore
	const userStore = store?.userStore

	const [products, setProducts] = useState<ProductProps[]>()
	const [user, setUser] = React.useState(userStore?.showUser);

	const history = useHistory();

	useEffect(() => {
		setUser(userStore?.loginUser({providerKey: Cookies.get("providerKey")}))

	}, [userStore, user?.providerKey]);

	useEffect(() => {
		(async () => {
			const result = toJS(await productsStore?.listProducts())

			setProducts(result)
		})();
	}, [productsStore])

	let urlElements = history.location.pathname.split('/')

	const prepareProducts = () => {
		return products?.map(product => {
			if(urlElements[urlElements.length-1] !== 'products' && Number(urlElements[urlElements.length-1]) === product.categoryId){
				return (
					<GridListTile key={uuidv4()} style={{minHeight: 400, minWidth: 370}}  cols={1}>
						<Product
							id={product.id}
							name={product.name}
							price={product.price}
							image={product.image}
							description={product.description}
							categoryId={product.categoryId}
						/>
					</GridListTile>
				);
			}
			else if(urlElements[urlElements.length-1] == 'products') {
				return (
					<GridListTile key={uuidv4()} style={{minHeight: 400, minWidth: 300}} cols={1}>
						<Product
							id={product.id}
							name={product.name}
							price={product.price}
							image={product.image}
							description={product.description}
							categoryId={product.categoryId}
						/>
					</GridListTile>
				);
			}
		});
	};

	return (
		<ProductListStyled>
			<div className='mt-5'>
				<GridList spacing={10} cols={3}>
					{prepareProducts()}
				</GridList>
			</div>
		</ProductListStyled>
	);
}));

const ProductListStyled = styled.div`
	minWidth: 80px;
`;
