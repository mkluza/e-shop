import {AppBar, IconButton, Toolbar, Typography, Button, ButtonGroup } from '@material-ui/core';
// @ts-ignore
import React, {FC} from 'react';
import {useHistory} from 'react-router';

export const Home: FC = ({children}) => {
    const history = useHistory()

    return (
        <>
            <AppBar position="static" color='secondary'>
    <Toolbar>
        <IconButton edge="start" onClick={() => history.push('/')}>
            <img height='100px' color='white' src='/images/icon.png' alt='icon'/>
        </IconButton>
        <Typography style={{flexGrow: 1, fontWeight: 600, fontSize: 50, textAlign: 'center'}} color='textPrimary'>
        Pet-Lover Shop
        </Typography>
    <div>
        <IconButton color='inherit' onClick={() => history.push('/')}>
            <img height='100px' src='/images/icon.png' alt='icon'/>
        </IconButton>
    </div>
    </Toolbar>
                <ButtonGroup variant="contained" color="primary">
                    <Button style={{width:'25%', fontSize: 20, fontWeight: 600}} onClick={() => history.push('/categories')}>Kategorie</Button>
                    <Button style={{width:'25%',fontSize: 20, fontWeight: 600}} onClick={() => history.push('/products')}>Produkty</Button>
                    <Button style={{width:'25%',fontSize: 20, fontWeight: 600}} onClick={() => history.push('/cart')}>Koszyk</Button>
                    <Button style={{width:'25%', fontSize: 20, fontWeight: 600}} onClick={() => history.push('/user')}>Moje konto</Button>
                </ButtonGroup>
            </AppBar>

    {children}
    </>
);
}