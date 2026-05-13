import { AdMobInitializationOptions, AdmobConsentInfo, } from "../../node_modules/@capacitor-community/admob/dist/esm";
import { AdmobConsentStatus as RealAdmobConsentStatus } from "../../node_modules/@capacitor-community/admob/dist/esm/consent/consent-status.enum";
import { AdmobConsentDebugGeography as RealAdmobConsentDebugGeography } from "../../node_modules/@capacitor-community/admob/dist/esm/consent/consent-debug-geography.enum";
import { PrivacyOptionsRequirementStatus } from "../../node_modules/@capacitor-community/admob/dist/esm/consent/privacy-options-requirement-status.enum";

export const AdMob = {
    async initialize(options?: AdMobInitializationOptions): Promise<void> {
        return Promise.resolve();
    },

    async requestConsentInfo(): Promise<AdmobConsentInfo> {
        return Promise.resolve({
            status: RealAdmobConsentStatus.REQUIRED,
            isConsentFormAvailable: true,
            canRequestAds: false,
            privacyOptionsRequirementStatus: PrivacyOptionsRequirementStatus.REQUIRED
        });
    },

    async showConsentForm(): Promise<AdmobConsentInfo> {
        return Promise.resolve({
            status: RealAdmobConsentStatus.OBTAINED,
            canRequestAds: true,
            privacyOptionsRequirementStatus: PrivacyOptionsRequirementStatus.REQUIRED
        });
    }
};

export const AdmobConsentStatus = RealAdmobConsentStatus;
export type AdmobConsentStatus = RealAdmobConsentStatus;

export const AdmobConsentDebugGeography = RealAdmobConsentDebugGeography;
export type AdmobConsentDebugGeography = RealAdmobConsentDebugGeography;
