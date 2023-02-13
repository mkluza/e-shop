// @ts-ignore
import React, {FC} from 'react';
import {v4 as uuidv4} from 'uuid';
import { Home } from '../components/Home';
import { Category } from '../components/Category';
import styled from "styled-components";

export const CategoryPage: FC = () => {

    return (
        <Home>
            <CategoryPageStyled key={uuidv4()}>
                <Category/>
            </CategoryPageStyled>
        </Home>
    );
};

const CategoryPageStyled = styled.div`
	display: flex;
	align-items: center;
	justify-content: center;
`;