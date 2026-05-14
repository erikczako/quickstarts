import { Component, OnInit } from '@angular/core';
import { IonHeader, IonToolbar, IonTitle, IonContent, IonButton } from '@ionic/angular/standalone';
import { AdMob, AdmobConsentDebugGeography, AdmobConsentStatus } from "@capacitor-community/admob";
import { environment } from 'src/environments/environment.prod';
import { PrivacyOptionsRequirementStatus } from '@capacitor-community/admob/dist/esm/consent/privacy-options-requirement-status.enum';

@Component({
    selector: 'app-home',
    template:`
    <ion-header [translucent]="true">
        <ion-toolbar>
            <ion-title>User Messaging Platform - UMP</ion-title>
        </ion-toolbar>
    </ion-header>

    <ion-content [fullscreen]="true">

        <div class="container">
            <div><strong>Consent Status = </strong>{{ status }}</div>
            <div><strong>Consent Form Available = </strong>{{ isConsentFormAvailable }}</div>
            <div><strong>Can Request Ads = </strong>{{ canRequestAds }}</div>
            <div><strong>Privacy Options Status = </strong>{{ privacyOptionsRequirementStatus }}</div>
            <div><ion-button (click)="showPrivacyOptionsForm()">Privacy Options</ion-button></div>
        </div>
    </ion-content>`,
    styleUrls: ['home.page.scss'],
    imports: [IonButton, IonHeader, IonToolbar, IonTitle, IonContent],
})
export class HomePage implements OnInit {

    status?: AdmobConsentStatus;
    isConsentFormAvailable?: boolean;
    canRequestAds?: boolean;
    privacyOptionsRequirementStatus?: PrivacyOptionsRequirementStatus;

    async ngOnInit() {
        await AdMob.initialize({ initializeForTesting: !environment.production });
        let consentInfo = await AdMob.requestConsentInfo({ debugGeography: AdmobConsentDebugGeography.EEA });

        this.status = consentInfo.status;
        this.isConsentFormAvailable = consentInfo.isConsentFormAvailable;
        this.canRequestAds = consentInfo.canRequestAds;
        this.privacyOptionsRequirementStatus = consentInfo.privacyOptionsRequirementStatus;

        consentInfo = await AdMob.showConsentForm();
        this.status = consentInfo.status;
        this.canRequestAds = consentInfo.canRequestAds;
        this.privacyOptionsRequirementStatus = consentInfo.privacyOptionsRequirementStatus;
    }

    showPrivacyOptionsForm() {
        AdMob.showPrivacyOptionsForm();
    }
}
