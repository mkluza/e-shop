// @ts-ignore
import React, {FC} from 'react';
import {v4 as uuidv4} from 'uuid';
import { Home } from '../components/Home';
import { User } from '../components/User';
import styled from "styled-components";

export const UserPage: FC = () => {

	return (
		<Home>
			<UserPageStyled key={uuidv4()}>
				<User/>
			</UserPageStyled>
		</Home>
	);
};

const UserPageStyled = styled.div`
	display: flex;
	align-items: center;
	justify-content: center;
`;