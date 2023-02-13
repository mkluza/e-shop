// @ts-ignore
import React, {FC} from 'react';
import {Button, Divider} from '@material-ui/core';
import {RootStore} from '../stores/RootStore';
import {inject, observer} from 'mobx-react';
import styled from "styled-components";
import {useHistory} from 'react-router';



export interface CartItemProps {
	id: number
	image: string
	name: string
	price: string
	amount: number
	cartId: number
}

export const CartItem: FC<{store?: RootStore} & CartItemProps> = inject("store")(observer(({
	store, id, image, name, price, amount, cartId
}) => {
	const cartStore = store?.cartStore

	const [showItem, setShowItem] = React.useState(true)
	const history = useHistory()


	const handleClick = () => {
		setShowItem(false)
		cartStore?.deleteCart(cartId, price)
	}

	let urlElements = history.location.pathname.split('/')

	if(showItem)
	{
		if(urlElements[urlElements.length-1] == 'cart') {
			return (
				<CartItemStyled>
					<div className='entries-row mb-4'>
						<img style={{height: 120, borderRadius: '10px', marginLeft: '30px'}} src={image}/>
						<h4 className='section' style={{marginLeft: '35px'}}>{name}</h4>
						<h4 className='section'>{price} ZŁ</h4>
						<h4 className='section'>{amount}</h4>
						<h4 className='section'>{Number(price)*amount} ZŁ</h4>
						<Button style={{marginRight: '20px', borderRadius: '20px', fontWeight: 600}} variant="contained" color='primary' onClick={() => {handleClick()}}>
							Usuń
						</Button>
					</div>
					<Divider style={{backgroundColor: 'white'}}/>
				</CartItemStyled>
			);
		}
		return (
			<CartItemStyled>
				<div className='entries-row mb-4'>
					<img style={{height: 120, borderRadius: '10px', marginLeft: '30px'}} src={image}/>
					<h4 className='section' style={{marginLeft: '35px'}}>{name}</h4>
					<h4 className='section'>{price} ZŁ</h4>
					<h4 className='section'>{amount}</h4>
					<h4 className='section'>{Number(price)*amount} ZŁ</h4>
				</div>
				<Divider style={{backgroundColor: 'white'}}/>
			</CartItemStyled>
		);
	}
	return (
		<div/>
	);

}));


const CartItemStyled = styled.div`
	padding: 10px;
	
	.entries-row {
		min-width: 10vw;
		display: flex;
		flex-direction: row;
		align-items: center;
		justify-content: space-between;
	}
	
	.section {
		min-width: 12vw;
		display: flex;
		flex-direction: column;
		align-items: center;
	}
`;