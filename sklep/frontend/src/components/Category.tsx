// @ts-ignore
import React, {FC, useEffect, useState} from 'react';
import {Button} from '@material-ui/core';
import {RootStore} from '../stores/RootStore';
import {inject, observer} from 'mobx-react';
import {toJS} from 'mobx';
import {CategoryProps} from "../stores/ProductsStore";
import {useHistory} from "react-router";
import Cookies from "js-cookie";
import styled from "styled-components";


export const Category: FC<{store?: RootStore}> = inject("store")(observer(({store}) => {
    const productsStore = store?.productsStore
    const userStore = store?.userStore

    const [categories, setCategories] = useState<CategoryProps[]>()
    const history = useHistory();

    const [user, setUser] = React.useState(userStore?.showUser);

    useEffect(() => {
        setUser(userStore?.loginUser({providerKey: Cookies.get("providerKey")}))

    }, [userStore, user?.providerKey]);


    useEffect(() => {
        (async () => {
            const result = toJS(await productsStore?.listCategory())
            setCategories(result)
        })();
    }, [productsStore])

    const handleClick = (id: number) => {
        history.push(`/category/${id}`)
    }

    const prepareCategories = () => {
        return categories?.map(category => {
            return (
                <CategoryStyled>
                    <Button style={{textAlign: 'center', width: 800, fontSize: 30, fontWeight: 600, boxShadow: '0 0 10px black'}} variant="contained" color='primary' onClick={() => handleClick(category.id)}>
                        {category.name}
                    </Button>
                </CategoryStyled>
            );
        });
    };

    return (
        <CategoryStyled>
            {prepareCategories()}
        </CategoryStyled>
    );
}));

const CategoryStyled = styled.div`
	padding: 25px;
	margin-top: 30px;
	width: '100%';
`;
