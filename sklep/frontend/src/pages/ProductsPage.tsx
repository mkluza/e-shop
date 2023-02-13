// @ts-ignore
import React, {FC} from 'react';
import { ProductList } from '../components/ProductList';
import {v4 as uuidv4} from 'uuid';
import { Home } from '../components/Home';
import styled from "styled-components";

export const ProductsPage: FC = () => {

    return (
        <Home>
            <ProductsPageStyled key={uuidv4()}>
            <ProductList/>
            </ProductsPageStyled>
        </Home>
    );
};

const ProductsPageStyled = styled.div`
	display: flex;
	align-items: center;
	justify-content: center;
`;