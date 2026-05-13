import { Component, OnInit } from '@angular/core';
import { IonHeader, IonToolbar, IonTitle, IonContent } from '@ionic/angular/standalone';
import { AdMob, AdmobConsentDebugGeography, AdmobConsentStatus } from "@capacitor-community/admob";
import { environment } from 'src/environments/environment.prod';
import { PrivacyOptionsRequirementStatus } from '@capacitor-community/admob/dist/esm/consent/privacy-options-requirement-status.enum';

@Component({
    selector: 'app-home',
    templateUrl: 'home.page.html',
    styleUrls: ['home.page.scss'],
    imports: [IonHeader, IonToolbar, IonTitle, IonContent],
})
export class HomePage implements OnInit {

    status?: AdmobConsentStatus;
    isConsentFormAvailable?: boolean;
    canRequestAds?: boolean;
    privacyOptionsRequirementStatus?: PrivacyOptionsRequirementStatus;

    async ngOnInit() {
        await AdMob.initialize({ initializeForTesting: true });
        let consentInfo = await AdMob.requestConsentInfo({ debugGeography: AdmobConsentDebugGeography.US });

        this.status = consentInfo.status;
        this.isConsentFormAvailable = consentInfo.isConsentFormAvailable;
        this.canRequestAds = consentInfo.canRequestAds;
        this.privacyOptionsRequirementStatus = consentInfo.privacyOptionsRequirementStatus;

        consentInfo = await AdMob.showConsentForm();
        this.status = consentInfo.status;
        this.canRequestAds = consentInfo.canRequestAds;
        this.privacyOptionsRequirementStatus = consentInfo.privacyOptionsRequirementStatus;
    }
}
