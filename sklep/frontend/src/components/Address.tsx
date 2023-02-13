// @ts-ignore
import React, {FC} from 'react';
import styled from "styled-components";
import {RootStore} from "../stores/RootStore";
import {inject, observer} from "mobx-react";
import {Divider, Typography} from "@material-ui/core";


export const Address: FC<{store?: RootStore}> = inject("store")(observer(({store}) => {
	const addressStore = store?.addressStore

	const address = addressStore?.showAddress()


	return (
		<AddressStyled style={{float: 'left', fontWeight: 300, fontSize: 60, borderRadius: '25px', backgroundColor: 'rgb(131,163,255)', color: 'white', textAlign: 'left',
			padding: 30, marginTop: 50, marginBottom: 30, boxShadow: '0 0 10px black'}}>
			<Typography style={{fontWeight: 200, fontSize: 50}}>
				Dane adresowe
			</Typography>
			<Divider style={{backgroundColor: 'white'}}/>
			<Typography style={{fontWeight: 200, fontSize: 30, marginTop: 10}}>
				Imię: {address.firstname}
			</Typography >
			<Typography style={{fontWeight: 200, fontSize: 30, marginTop: 10}}>
				Nazwisko: {address.lastname}
			</Typography>
			<Typography style={{fontWeight: 200, fontSize: 30, marginTop: 10}}>
				Miasto: {address.city}
			</Typography>
			<Typography style={{fontWeight: 200, fontSize: 30, marginTop: 10}}>
				Kod pocztowy: {address.zipcode}
			</Typography>
			<Typography style={{fontWeight: 200, fontSize: 30, marginTop: 10}}>
				Ulica: {address.street}
			</Typography>
			<Typography style={{fontWeight: 200, fontSize: 30, marginTop: 10}}>
				Numer telefonu: {address.phoneNumber}
			</Typography>
		</AddressStyled>
	);
}))

const AddressStyled = styled.div`
	padding: 10px
	margin-bottom: 30px;
`;
