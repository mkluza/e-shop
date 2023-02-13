// @ts-ignore
import React, {FC} from 'react';
import {Cart} from '../components/Cart';
import { v4 as uuidv4 } from "uuid";
import { Home } from '../components/Home';
import styled from "styled-components";
import {Address} from "../components/Address";

export const OrderPage: FC = () => {

	return (
		<Home>
			<OrderPageStyled key={uuidv4()}>
				<div className="row" style={{margin: 'auto', width: '50%', textAlign: 'center'}}>
					<Address/>
				</div>
				<div className="row" style={{margin: 'auto',width: '80%', textAlign: 'center'}}>
					<Cart/>
				</div>
			</OrderPageStyled>
		</Home>
	);
};


const OrderPageStyled = styled.div`
`;