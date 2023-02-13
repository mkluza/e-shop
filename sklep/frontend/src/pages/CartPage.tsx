// @ts-ignore
import React, {FC} from 'react';
import {Cart} from '../components/Cart';
import { v4 as uuidv4 } from "uuid";
import { Home } from '../components/Home';
import styled from "styled-components";

export const CartPage: FC = () => {

	return (
		<Home>
			<CartPageStyled key={uuidv4()}>
				<Cart/>
			</CartPageStyled>
		</Home>
	);
};


const CartPageStyled = styled.div`
	display: flex;
	align-items: center;
	justify-content: center;
`;