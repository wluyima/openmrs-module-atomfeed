package org.openmrs.module.atomfeed.api.impl;

import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.atomfeed.AtomfeedmoduleConstants;
import org.openmrs.module.atomfeed.api.FeedConfigurationService;
import org.openmrs.module.atomfeed.api.model.FeedConfiguration;
import org.openmrs.module.atomfeed.api.utils.AtomfeedUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

@Component("atomfeed.feedConfigurationService")
public class FeedConfigurationServiceImpl extends BaseOpenmrsService implements FeedConfigurationService {

    private HashMap<String, FeedConfiguration> feedConfiguration;

    public FeedConfigurationServiceImpl() {
        if (AtomfeedUtils.resourceFileExists(AtomfeedmoduleConstants.ATOMFEED_PATH_TO_LOCAL_CONFIGURATION)) {
            loadFeedConfigurations(
                    AtomfeedUtils.parseJsonFileToFeedConfiguration(AtomfeedmoduleConstants.ATOMFEED_PATH_TO_LOCAL_CONFIGURATION)
            );
        } else {
            loadFeedConfigurations(
                    AtomfeedUtils.parseJsonFileToFeedConfiguration(AtomfeedmoduleConstants.ATOMFEED_PATH_TO_DAFAULT_CONFIGURATION)
            );
        }
    }

    public void saveConfig(List<FeedConfiguration> value) {
        AtomfeedUtils.writeFeedConfigurationToJsonFile(value,
            AtomfeedmoduleConstants.ATOMFEED_PATH_TO_LOCAL_CONFIGURATION);
        loadFeedConfigurations(value);
    }

    public void saveConfig(String value) {
        if (AtomfeedUtils.isValidateJson(value)) {
            List<FeedConfiguration> localConfiguration = AtomfeedUtils.parseJsonStringToFeedConfiguration(value);
            AtomfeedUtils.writeFeedConfigurationToJsonFile(localConfiguration,
                AtomfeedmoduleConstants.ATOMFEED_PATH_TO_LOCAL_CONFIGURATION);
            loadFeedConfigurations(localConfiguration);
        }
    }

    public FeedConfiguration getFeedConfigurationByTitle(String title) {
        return feedConfiguration.get(title);
    }

    private void loadFeedConfigurations(List<FeedConfiguration> feedConfigurations) {
        HashMap<String, FeedConfiguration> tmp = new HashMap<>();
        for (FeedConfiguration configuration : feedConfigurations) {
            tmp.put(configuration.getTitle(), configuration);
        }
        feedConfiguration = tmp;
    }
}
