// @ts-ignore
import React, {FC} from 'react';
import {v4 as uuidv4} from 'uuid';
import { Home } from '../components/Home';
import styled from "styled-components";
import {Typography} from '@material-ui/core';


export const HomePage: FC = () => {

	return (
		<Home>
			<HomePageStyled key={uuidv4()}>
			</HomePageStyled>
			<div style={{float: 'left', width: '60%'}}>
				<Typography style={{flexGrow: 1, fontWeight: 600, fontSize: 60, textAlign: 'center', marginTop: 140, marginLeft: '20px'}} color='primary'>
					Pet-Lover Shop
				</Typography>
				<Typography style={{flexGrow: 1, fontWeight: 300, fontSize: 20, textAlign: 'center', marginTop: 30, marginLeft: '20px'}} color='primary'>
					Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.
					Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.
					Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.
					Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.
				</Typography>
			</div>
				<img style={{width: 400, marginTop: 100, marginLeft:100}} src='/images/icon_2.png'/>
		</Home>

	);
};

const HomePageStyled = styled.div`
	align-items: center;
	justify-content: center;
`;