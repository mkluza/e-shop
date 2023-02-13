// @ts-ignore
import React, {FC} from 'react';
import {v4 as uuidv4} from 'uuid';
import { Home } from '../components/Home';
import styled from "styled-components";
import {ProductCheck} from "../components/ProductCheck";

export const ProductPage: FC = () => {

    return (
        <Home>
            <ProductPageStyled key={uuidv4()}>
                <ProductCheck/>
            </ProductPageStyled>
        </Home>
    );
};

const ProductPageStyled = styled.div`
	display: flex;
	align-items: center;
	justify-content: center;
`;