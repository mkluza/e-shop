// @ts-ignore
import React, {FC} from 'react';
import {v4 as uuidv4} from 'uuid';
import { Home } from '../components/Home';
import styled from "styled-components";
import {Typography} from '@material-ui/core';


export const ThanksPage: FC = () => {

    return (
        <Home>
            <HomePageStyled key={uuidv4()}>
                <div style={{float: 'left'}}>
                <Typography style={{flexGrow: 1, fontWeight: 600, fontSize: 50, textAlign: 'center', marginTop: '175px', marginLeft: '20px'}} color='primary'>
                    Dziękujemy za wybranie naszego sklepu!
                </Typography>
                <Typography style={{ flexGrow: 1, fontWeight: 300, fontSize: 20, textAlign: 'center', marginTop: 20, marginLeft: '20px'}} color='primary'>
                    Zamówione produkty zostaną dostarczone na wskazany adres w ciągu 5 dni roboczych.
                </Typography>
                </div>
                <img style={{minWidth: 400, width: 400, marginTop: 50, marginLeft: 50}}  color='secondary' src='/images/icon_2.png' alt='icon'/>
            </HomePageStyled>

        </Home>

    );
};

const HomePageStyled = styled.div`
	align-items: center;
	justify-content: center;
`;