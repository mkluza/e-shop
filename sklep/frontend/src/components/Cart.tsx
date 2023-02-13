// @ts-ignore
import React, {FC, useEffect, useState} from 'react';
import {CartItem} from './CartItem';
import {Button, Dialog, DialogTitle, Divider} from '@material-ui/core';
import {inject, observer} from 'mobx-react';
import {RootStore} from '../stores/RootStore';
import styled from "styled-components";
import {CartProps} from "../stores/CartStore";
import {toJS} from "mobx";
import {ProductProps} from "./Product";
import Cookies from "js-cookie";
import {useHistory} from 'react-router';


export const Cart: FC<{store?: RootStore}> = inject("store")(observer(({store}) => {
	const cartStore = store?.cartStore
	const userStore = store?.userStore
	const productsStore = store?.productsStore
	const addressStore = store?.addressStore
	const orderStore = store?.orderStore

	const history = useHistory()

	const [carts, setCarts] = useState<CartProps[]>()
	const [products, setProducts] = useState<ProductProps[]>()
	const [open, setOpen] = React.useState(false);

	const user = userStore?.showUser()
	const address = addressStore?.showAddress()

	useEffect(() => {
		(async () => {
			userStore?.loginUser({providerKey: Cookies.get("providerKey")})

			if(user.providerKey === '' || user.providerKey === undefined) {
				return history.push(`/user`)
			}

			addressStore?.setAddress(user?.providerKey)
			const result = toJS(await cartStore?.listCarts(user?.providerKey))
			setCarts(result)

		})();
	}, [history, user.providerKey, userStore, cartStore])



	useEffect(() => {
		(async () => {
			const result = toJS(await productsStore?.listProducts())
			setProducts(result)
		})();
	}, [productsStore])


	const prepareProducts = () => {
		return products?.map( product => {
			return carts?.map(cart => {
				if(product.id == cart.productId){
					return (
						<CartItem id={product.id} image={product.image} name={product.name} price={product.price} amount={cart.amount} cartId={cart.id}>
						</CartItem>
					)
				}
			});
		});
	}

	const handleClick = () => {
		if(carts?.length != 0)
		{
			return history.push(`/order`)
		}

		setOpen(true);
		setTimeout(() => {
			handleClose()
		}, 1000)
	}

	const handleOrder = () => {
		products?.map( product => {
			carts?.map(cart => {
				if(product.id == cart.productId){
					orderStore?.addOrder({id:1, cartId: cart.id, addressId: address.id})
					cartStore?.deleteCart(cart.id, product.price)
				}
			});
		});

		return history.push(`/thanks`)
	}

	const handleClose = () => {
		setOpen(false);
	};

	let urlElements = history.location.pathname.split('/')
	if(urlElements[urlElements.length-1] == 'cart') {
		return (
			<CartStyled>
				<div className='mt-5 entries'>
					<div className="row" style={{fontSize: 30}}>
						<div className="col-3"/>
						<div className="col-2">Nazwa</div>
						<div className="col-2">Cena</div>
						<div className="col-2">Ilość</div>
						<div className="col-2">Łącznie</div>
						<div className="col-1"/>
					</div>
					<Divider style={{backgroundColor: 'white'}}/>
					{prepareProducts()}
					<div className='p-4'>
						<h4 className='summary' style={{marginLeft: '51vw', display: 'inline'}}>Do
							zapłaty: {cartStore?.showPrice()} PLN</h4>
						<Button onClick={() => handleClick()} style={{height: '40px', borderRadius: '20px'}}
								variant="contained" color='primary'>Idź do kasy</Button>
					</div>
				</div>
				<Dialog
					open={open}
					onClose={handleClose}>
					<DialogTitle style={{color: 'black'}} color='secondary'>{"Koszyk jest pusty"}</DialogTitle>
				</Dialog>
			</CartStyled>
		);
	}
	return (
		<CartStyled>
			<div className='mt-5 entries'>
				<div className="row" style={{fontSize: 30, fontWeight: 600}}>
					<div className="col-3"/>
					<div className="col-2">Nazwa</div>
					<div className="col-2">Cena</div>
					<div className="col-2">Ilość</div>
					<div className="col-2">Łącznie</div>
					<div className="col-1"/>
				</div>
				{prepareProducts()}
				<div className='p-4'>
					<h4 className='summary' style={{marginLeft: '51vw', display: 'inline'}}>Do
						zapłaty: {cartStore?.showPrice()} PLN</h4>
					<Button onClick={() => handleOrder()} style={{height: '40px', borderRadius: '20px', fontWeight: 600}}
							variant="contained" color='primary'>Potwierdź zamówienie</Button>
				</div>
			</div>
		</CartStyled>
	);
}));


const CartStyled = styled.div`
	display: flex;
	flex-direction: column;
	align-items: center;
	justify-content: center;
	margin-bottom: 30px;
	
	.entries {
		padding: 10px;
		box-shadow: 0 0 10px black;
		background-color: rgb(131,163,255);
		border-radius: 4px;
	}

	.head {
		padding: 5px;
		background-color: rgb(62,102,167);
		box-shadow: 0 0 2px black;
		display: flex;
		flex-direction: row;
		border-radius: 1px;
		margin-bottom: 10px;
	}

	.section {
		min-width: 15vw;
		display: flex;
		flex-direction: column;
		align-items: center;
		margin-bottom: 0;
	}
	
	.summary {
		padding: 10px;
		text-align: right;
		margin-right: 5vw;
	}
`;
