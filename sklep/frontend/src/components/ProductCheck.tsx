// @ts-ignore
import React, {FC, useEffect} from 'react';
import {
    Button,
    Dialog,
    Typography,
    DialogTitle,
} from '@material-ui/core';
import {RootStore} from '../stores/RootStore';
import {inject, observer} from 'mobx-react';
import styled from "styled-components";
import {useHistory} from "react-router";
import Cookies from "js-cookie";


export const ProductCheck: FC<{store?: RootStore }> = inject("store")(observer(({store}) => {
    const productStore = store?.productStore
    const userStore = store?.userStore
    const cartStore = store?.cartStore

    const history = useHistory();
    const product = productStore?.showProduct()

    const [open, setOpen] = React.useState(false);
    const [user, setUser] = React.useState(userStore?.showUser);

    useEffect(() => {
        let urlElements = history.location.pathname.split('/')
        setUser(userStore?.loginUser({providerKey: Cookies.get("providerKey")}))

        productStore?.checkProduct(Number(urlElements[urlElements.length-1]))
    }, [userStore, user?.providerKey, history.location.pathname, productStore]);


    const handleClickOpen = () => {
        setOpen(true);
        setTimeout(() => {
            handleClose()
        }, 1000)
    };

    const handleClose = () => {
        setOpen(false);
    };

    const handleClick = () => {

        if(user?.providerKey == '' || user?.providerKey == undefined) {
            return history.push(`/user`)
        }

        cartStore?.addCart({id: 1, providerKey: user?.providerKey!.toString(), productId: product.id, amount:1}, product.price)
        handleClickOpen()
    }


    return (
        <ItemStyled>
                <img style={{width: '30%', float: 'left', borderRadius: '10px', marginTop: 50, marginLeft: 50}} src={product.image}/>

                <div style={{width: '60%', float: 'right', marginRight: 50, marginTop: 30, textAlign: 'center'}}>
                    <Typography style={{flexGrow: 1, fontWeight: 600, fontSize: 50, color: 'rgb(62,102,167)', textAlign: 'center'}}>
                        {product.name}
                    </Typography>
                    <Typography style={{flexGrow: 1, fontWeight: 400, fontSize: 30, color: 'rgb(62,102,167)', textAlign: 'center'}}>
                            Cena:                {product.price} ZŁ
                    </Typography>
                    <Button size="large" style={{width: '50%', marginTop: 30, fontWeight: 600, boxShadow: '0 0 10px black'}} variant="contained" color='primary' onClick={() => handleClick()}>
                        Do koszyka
                    </Button>
                    <Typography style={{flexGrow: 1, fontWeight: 300, fontSize: 20, borderRadius: '25px', backgroundColor: 'rgb(131,163,255)', color: 'white', textAlign: 'center',
                        padding: 20, marginTop: 30, boxShadow: '0 0 10px black'}}>
                        {product.description}
                    </Typography>
                </div>
                <Dialog
                    open={open}
                    onClose={handleClose}>
                    <DialogTitle style={{color: 'black'}} color='secondary'>{"Dodano do koszyka"}</DialogTitle>
                </Dialog>
    </ItemStyled>
);
}));


const ItemStyled = styled.div`
	padding: 10px;
	
	.img {
	    display: block;
        margin-left: auto;
        margin-right: auto;
        width: 50%;
	}
	.entries-row {
		min-width: 10vw;
		display: flex;
		flex-direction: row;
		align-items: center;
		justify-content: space-between;
	}
	
	.section {
		min-width: 12vw;
		display: flex;
		flex-direction: column;
		align-items: center;
	}
`;
