// @ts-ignore
import React, {FC, useEffect} from 'react';
import {
	Button,
	Dialog,
	DialogContent,
	DialogContentText,
	DialogTitle,
	Input
} from '@material-ui/core';
import {Field, Form, Formik, FormikHelpers} from 'formik';
import styled from 'styled-components';
import {RootStore} from "../stores/RootStore";
import {inject, observer} from "mobx-react";
import Cookies from "js-cookie";
import {Address} from "./Address";

export const User: FC<{store?: RootStore }> = inject("store")(observer(({store}) => {
	const userStore = store?.userStore
	const addressStore = store?.addressStore

	const user = userStore?.showUser()
	const address = addressStore?.showAddress()

	useEffect(() => {
		userStore?.loginUser({providerKey: Cookies.get("providerKey")})
	}, [user?.providerKey, userStore]);

	useEffect(() => {
		if(user?.providerKey !== '' && user?.providerKey !== undefined) {
			addressStore?.addAddress(Cookies.get("providerKey"))
		}
	}, [addressStore, user?.providerKey]);

	const handleLogin = () => {
		userStore?.loginUser({providerKey: Cookies.get("providerKey")})
	};

	const handleLogout = () => {
		document.cookie.split(";").forEach(function (c) {
			document.cookie = c
				.replace(/^ +/, "")
				.replace(/=.*/, "=;expires=" + new Date().toUTCString() + ";path=/");
		});
		userStore?.logoutUser();
	};

	const onLogin = async (provider: string) => {
		if (provider === 'google') {
			window.location.assign(
				"http://localhost:9000" + "/authenticate/google"
			);
		}
		if (provider === 'github') {
			window.location.assign(
				"http://localhost:9000" + "/authenticate/github"
			);
		}
		handleLogin()
	};


	const [open, setOpen] = React.useState(false);

	const handleClickOpen = () => {
		setOpen(true);
	};

	const handleClose = () => {
		setOpen(false);
	};

	const onSubmit = async (data: {
		id: number,
		providerKey: string | undefined,
		firstname: string,
		lastname: string,
		city: string,
		zipcode: string,
		street: string,
		phoneNumber: string,
	}, actions: FormikHelpers<any>) => {
		actions.resetForm();

		addressStore?.updateAddress(data)
	};

	if(user?.providerKey == '' || user?.providerKey == undefined) {
		return (
			<UserStyled>
				<div style={{backgroundColor: 'rgb(131,163,255)', borderRadius: '15%', width: 400, height: 300, textAlign: 'center', boxShadow: '0 0 10px black'}}>
					<div>
						<Button onClick={() => {
							onLogin('google')
						}} style={{width: 300, borderRadius: '20px', fontWeight: 600, marginTop: 70, fontSize: 20, boxShadow: '0 0 10px black'}} variant="contained" color='primary'> Zaloguj przez
							Google</Button>

						<Button onClick={() => {
							onLogin('github')
						}} style={{width: 300, borderRadius: '20px', fontWeight: 600, marginTop: 70, fontSize: 20, boxShadow: '0 0 10px black'}} variant="contained" color='primary'> Zaloguj przez
							GitHuba
						</Button>
					</div>
				</div>
			</UserStyled>
		);
	}

	return (
		<div>
			<div className="container">
				<div className="row">
					<div className="col">
						<Address/>
					</div>
					<div className="col" style={{textAlign: 'center'}} >
							<Button size="large" onClick={handleClickOpen} style={{ padding: 15, borderRadius: '20px', fontWeight: 600, marginTop: 150, fontSize: 20, boxShadow: '0 0 10px black', width: 400}} variant="contained" color='primary'>
								Edytuj
							</Button>
							<Button size="large" style={{ marginLeft: 0, padding: 15, borderRadius: '20px', fontWeight: 600, marginTop: 100, fontSize: 20, boxShadow: '0 0 10px black', width: 400}} variant="contained" color='primary' onClick={() => handleLogout()}>
								Wyloguj
							</Button>
					</div>
				</div>
			</div>

			<Dialog open={open} onClose={handleClose} style={{minWidth:800, minHeight: 800, backgroundColor: 'rgb(131,163,255)', textAlign: 'center', boxShadow: '0 0 10px black', marginBottom: 20}} >
			<div style={{minHeight:700, backgroundColor: 'rgb(131,163,255)', textAlign: 'center', marginBottom: 20}}>
			<DialogTitle style={{fontWeight: 600, fontSize: 30}}>Dane kontaktowe</DialogTitle>
			<DialogContent style={{}}>
			<Formik initialValues={{
							id: address.id,
							providerKey: address.providerKey,
							firstname: address.firstname,
							lastname: address.lastname,
							city: address.city,
							zipcode: address.zipcode,
							street: address.street,
							phoneNumber: address.phoneNumber,
						}}
								onSubmit={(values, actions) => onSubmit(values, actions)}>
							{({errors, touched, values, isSubmitting}) => (
								<Form >
									<DialogContentText style={{color: "white"}}>Imię:</DialogContentText>
									<Field as={Input} style={{minWidth: 200, backgroundColor: 'white', color: "black", boxShadow: '0 0 5px black'}} name='firstname'/>

									<DialogContentText style={{color: "white", marginTop:20}}>Nazwisko:</DialogContentText>
									<Field as={Input} style={{minWidth: 200, backgroundColor: 'white', color: "black", boxShadow: '0 0 5px black'}} name='lastname'/>

									<DialogContentText style={{color: "white", marginTop:20}}>Miasto:</DialogContentText>
									<Field as={Input} style={{minWidth: 200, backgroundColor: 'white', color: "black", boxShadow: '0 0 5px black'}} name='city'/>

									<DialogContentText style={{color: "white", marginTop:20}}>Kod pocztowy:</DialogContentText>
									<Field as={Input} style={{minWidth: 200, backgroundColor: 'white', color: "black", boxShadow: '0 0 5px black'}} name='zipcode'/>

									<DialogContentText style={{color: "white", marginTop:20}}>Ulica:</DialogContentText>
									<Field as={Input} style={{minWidth: 200, backgroundColor: 'white', color: "black", boxShadow: '0 0 5px black'}} name='street'/>

									<DialogContentText style={{color: "white", marginTop:20}}>Numer telefonu:</DialogContentText>
									<Field as={Input} style={{minWidth: 200, backgroundColor: 'white', color: "black", boxShadow: '0 0 5px black'}} name='phoneNumber'/>

									<div  style={{textAlign: 'center'}}>
									<Field as={Button} onClick={handleClose} type='submit' disabled={isSubmitting}
									   style={{width: 150, borderRadius: '20px', fontWeight: 600, marginTop:20, textAlign: 'center', boxShadow: '0 0 10px black'}} variant="contained" color='primary'>Aktualizuj</Field>
									</div>
								</Form>
							)}
						</Formik>
					</DialogContent>
				</div>
				</Dialog>
		</div>
	)
}));

const UserStyled = styled.div`
	padding: 25px;
	margin-top: 80px;
`;
